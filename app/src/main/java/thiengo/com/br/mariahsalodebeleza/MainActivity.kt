package thiengo.com.br.mariahsalodebeleza

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.activity_main )
        setSupportActionBar( toolbar )

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener( toggle )
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener( this )
    }

    override fun onResume() {
        super.onResume()
        /*
         * Hackcode para que seja possível atualizar o título
         * da barra de topo sem que seja necessário mudar o
         * nome do aplicativo.
         * */
        toolbar.title = getString(R.string.label_address)
    }

    override fun onBackPressed() {
        if( drawer_layout.isDrawerOpen( GravityCompat.START ) ) {
            drawer_layout.closeDrawer( GravityCompat.START )
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected( item: MenuItem ): Boolean {
        /*
         * Foi deixado aqui dentro somente o necessário para
         * fechar o menu gaveta quando algum intem for acionado.
         * */
        drawer_layout.closeDrawer( GravityCompat.START )
        return false // Para não mudar o item selecionado em menu gaveta
    }

    /*
     * Método ouvidor para permitir que o usuário entre em contato
     * com o WhatsApp correto com apenas um acionamento em tela.
     * */
    fun whatsAppHelp( view: View ){
        /* O número abaixo é fictício. */
        val whatsAppUri = Uri.parse( "smsto:27999887766" )
        val intent = Intent( Intent.ACTION_SENDTO, whatsAppUri )

        intent.setPackage( "com.whatsapp" )

        /*
         * Garantindo que a Intent somente será acionada se o
         * aplicativo WhatsApp estiver presente no aparelho.
         * */
        if( intent.resolveActivity( packageManager ) != null ){
            startActivity( intent )
        }
        else{
            Toast
                .makeText(
                    this,
                    getString(R.string.whatsapp_needed_info),
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    /*
     * Método listener de toque (clique) no botão "VISUALIZAR ROTA",
     * responsável por invocar o Google Maps para apresentar ao
     * usuário a rota que ele terá de percorrer até o salão de
     * beleza, isso partindo do ponto atual dele. Como o salão de
     * beleza é ficticio, está sendo utilizada uma estética presente
     * em Morada de Larajeiras, Serra, ES.
     * */
    fun showRoute(view: View){
        var beautySalon = "Rebecca Miranda Centro Estético, " +
                "Morada de Laranjeiras, Serra, Espírito Santo, Brasil"
        beautySalon = Uri.encode( beautySalon )

        var navigation = "google.navigation:q=$beautySalon"

        var navigationUri = Uri.parse( navigation )
        var intent = Intent( Intent.ACTION_VIEW, navigationUri )

        intent.setPackage( "com.google.android.apps.maps" )

        /*
         * Caso o aplicativo do Google Maps não esteja presente no
         * aparelho (algo difícil de acontecer), partimos para a
         * apresentação de rota pelo Google Maps Web, via navegador
         * mobile.
         * */
        if( intent.resolveActivity( packageManager ) == null ){

            val dirAction = "dir_action=navigate"
            val destination = "destination=$beautySalon"
            navigation = "https://www.google.com/maps/dir/?api=1&$dirAction&$destination"

            navigationUri = Uri.parse( navigation )
            intent = Intent( Intent.ACTION_VIEW, navigationUri )
        }

        if( intent.resolveActivity( packageManager ) != null ){

            startActivity( intent )
        }
        else{
            /*
             * Se nem Google Maps e nem navegador mobile estiverem
             * presentes no aparelho, informe ao usuário para
             * instalar ao menos um dos dois.
             * */
            Toast
                    .makeText(
                            this,
                            getString( R.string.apps_needed_info ),
                            Toast.LENGTH_LONG
                    )
                    .show()
        }
    }
}